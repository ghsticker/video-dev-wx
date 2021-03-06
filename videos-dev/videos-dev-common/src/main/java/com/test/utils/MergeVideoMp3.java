package com.test.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @author ghsticker
 * 2019年3月5日
 */
public class MergeVideoMp3 {

	private String ffmpegEXE;
	
	public MergeVideoMp3(String ffmpegEXE) {
		super();
		this.ffmpegEXE = ffmpegEXE;
	}
	
	public void convertor(String videoInputPath,String mp3InputPath,Double seconds,
			String videoOutputPath) throws IOException{
		//ffmpeg.exe  -i bgm.mp3 -i input.mp4 -t 8 -y output.mp4
		List<String> command = new ArrayList<String>();
		
		command.add(ffmpegEXE);
		
		command.add("-i");
		command.add(mp3InputPath);
		
		command.add("-i");
		command.add(videoInputPath);
	
		command.add("-t");
		command.add(String.valueOf(seconds));
		
		command.add("-y");
		command.add(videoOutputPath);
		
		for (String string : command) {
			System.out.print(string);
		}
		ProcessBuilder builder = new ProcessBuilder(command);
		
		Process process = builder.start();
		//读取释放流
		InputStream errorStream = process.getErrorStream();
		InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
		BufferedReader br = new BufferedReader(inputStreamReader);
		
		String line = "";
		while((line = br.readLine())!=null){
			
		}
		
		if(errorStream!=null){
			errorStream.close();
		}
		if(inputStreamReader!=null){
			inputStreamReader.close();
		}
		if(br!=null){
			br.close();
		}

	}
	
	
	public static void main(String[] args) {
		
		MergeVideoMp3 ffmpeg = new MergeVideoMp3("E:\\ffmpeg\\bin\\ffmpeg.exe");
		
		try {
			ffmpeg.convertor("D:\\input.mp4","D:\\Time.mp3",40.0, "D:\\output.mp4");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

