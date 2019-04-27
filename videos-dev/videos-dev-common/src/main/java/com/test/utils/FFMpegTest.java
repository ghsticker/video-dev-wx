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
public class FFMpegTest {

	private String ffmpegEXE;
	
	public FFMpegTest(String ffmpegEXE) {
		super();
		this.ffmpegEXE = ffmpegEXE;
	}
	
	public void convertor(String videoInputPath,String videoOutputPath) throws IOException{
		//ffmpeg -i input.mp4 output.avi
		List<String> command = new ArrayList<String>();
		
		command.add(ffmpegEXE);
		command.add("-i");
		command.add(videoInputPath);
		command.add(videoOutputPath);
		
		for (String string : command) {
			System.out.println(string);
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
		
		FFMpegTest ffmpeg = new FFMpegTest("E:\\ffmpeg\\bin\\ffmpeg.exe");
		
		try {
			ffmpeg.convertor("D:\\input.mp4", "D:\\output.avi");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

