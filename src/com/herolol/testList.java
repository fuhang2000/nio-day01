package com.herolol;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

public class testList {

	
	
	
	@Test
	public void test2() throws IOException {
		
		FileChannel infchannel= FileChannel.open(
				Paths.get("H:\\java\\nio\\Java NIO.pdf"), StandardOpenOption.READ);
		
	FileChannel outchannle=FileChannel.open(
				Paths.get("H:\\java\\nio\\Java NIOs.pdf"), StandardOpenOption.READ,StandardOpenOption.WRITE, StandardOpenOption.CREATE);
			
	infchannel.transferFrom(outchannle, 0, infchannel.size());
	infchannel.close();
	outchannle.close();
	}
	
	
	
	@Test
	public  void tset1() throws IOException {
		
		
		FileInputStream fis=new FileInputStream("H:\\test\\1\\1.png");
		
		
		FileOutputStream fos=new FileOutputStream("H:\\test\\1\\4.png");
		
		
		FileChannel ic=fis.getChannel();
		
		FileChannel oc=fos.getChannel();
		
		ByteBuffer buf=ByteBuffer.allocate(1024);
		
		
		while(ic.read(buf)!=-1) {
			
			buf.flip();
			oc.write(buf);
			
			buf.clear();
			
		}
		oc.close();
		ic.close();
		fis.close();
		fos.close();
		
		
	}
}
