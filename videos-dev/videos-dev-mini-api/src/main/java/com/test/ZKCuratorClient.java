package com.test;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.test.cofig.ResourceConfig;
import com.test.enums.BGMOperatorTypeEnum;
import com.test.pojo.Bgm;
import com.test.service.BgmService;
import com.test.utils.JsonUtils;

/**
 * @Description
 * @author ghsticker
 * 2019年3月13日
 */
@Component
public class ZKCuratorClient {
	// zk客户端
	private CuratorFramework client = null;	
	final static Logger log = LoggerFactory.getLogger(ZKCuratorClient.class);

//	@Autowired
//	private BgmService bgmService;
//	
//	private static final String ZOOKEEPER_SERVER = "192.168.26.128:2181";
//	
	@Autowired
	private ResourceConfig resourceConfig;
	
	public void init(){
		if(client!=null){
			return;
		}
		//重试策略
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(10000, 5);
		//创建ZK客户端
		client = CuratorFrameworkFactory.builder().connectString(resourceConfig.getZookeeperServer())
				.sessionTimeoutMs(10000)
				.retryPolicy(retryPolicy)
				.namespace("admin")
				.build();
		
		client.start();
		
		try {
//			String test = new String(client.getData().forPath("/bgm/1903138Z4KMRTXP2"));
//			log.info(test);
			addChildWatch("/bgm");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void addChildWatch(String nodePath) throws Exception{
		PathChildrenCache cache = new PathChildrenCache(client, nodePath, true);
		cache.start();
		
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				
				if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
					log.info("监听到事件 CHILD_ADDED");
					
					// 1. 从数据库查询bgm对象，获取路径path1
					String path = event.getData().getPath();
					//String operatorObjStr = new String(event.getData().getData(),"UTF-8"); 
					String operatorObjStr = new String(event.getData().getData());
					Map<String, String> map = JsonUtils.jsonToPojo(operatorObjStr, Map.class);
					String operatorType = map.get("operType");
					String songPath = map.get("path");// \bgm\Time.mp3
					//songPath = new String(songPath.getBytes(),"UTF-8");
//					String arr[] = path.split("/");
//					String bgmId = arr[arr.length - 1];
					
//					Bgm bgm = bgmService.queryBgmById(bgmId);
//					if (bgm == null) {
//						return;
//					}
					
					// 1.1 bgm所在的相对路径
//					String songPath = bgm.getPath();
					
					// 2. 定义保存到本地的bgm路径
					//String filePath = "C:/videos-dev" + songPath;
					String filePath = resourceConfig.getFileSpace() + songPath;
					
					// 3. 定义下载的路径（播放url）
					//String arrPath[] = songPath.split("/");  //linux
					String arrPath[] = songPath.split("\\\\");  //window
					String finalPath = "";
					// 3.1 处理url的斜杠以及编码
					for(int i = 0; i < arrPath.length ; i ++) {
						if (StringUtils.isNotBlank(arrPath[i])) {
							finalPath += "/";
							finalPath += URLEncoder.encode(arrPath[i],"utf-8");
							//System.out.println(finalPath);
						}
					}
//					String bgmUrl = "http://localhost:8080/mvc" + finalPath;
					String bgmUrl = resourceConfig.getBgmServer() + finalPath;
					//bgmUrl = URLEncoder.encode(bgmUrl, "UTF-8");
					if (operatorType.equals(BGMOperatorTypeEnum.ADD.type)) {
						// 下载bgm到spingboot服务器
						URL url = new URL(bgmUrl);
						//filePath = URLEncoder.encode(filePath,"UTF-8");
						File file = new File(filePath);
						System.out.println("保存文件的地址"+filePath);
						System.out.println("bgm下载地址"+bgmUrl);
						FileUtils.copyURLToFile(url, file);
						System.out.println("下载成功----------");
						client.delete().forPath(path);
					} else if (operatorType.equals(BGMOperatorTypeEnum.DELETE.type)) {
						File file = new File(filePath);
						System.out.println(filePath);
						FileUtils.forceDelete(file);
						client.delete().forPath(path);
					}
				}

			}
		});
		
	}
}








