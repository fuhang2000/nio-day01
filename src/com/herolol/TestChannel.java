package com.herolol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 *1; 通道 channel:y用于源节点与目标节点的链接.在java NIO 中 负责缓冲区中数据的传输.
 * Channel不存储数据.需要配合缓冲区进行传输.
 * 
 *2;通道主要实现类:
 *java.nio.channeks.Channel借口:
 *--FileChannel;
 *SokcetChannel;
 *ServerSocketChannel;
 *DatagramChannel;  udp
 *
 *3;获取通道"
 *   1;java 针对支持通道的类提供了 getChannel()方法
 *      FileInputStream /FileoutputStream
 *      randomAccessFile
 *      网络IO:
 *      socket,ServerSocket
 *      DatagramSocket;
 *   2; 在JDK7 中 的 nio.2 针对各个通道提供了静态方法 open()
 *   3; 在JDK7 中 的 nio.2 的Files 工具类中  newByteChannel();  
 *   
 * 4 通道之间的传输
 * transferFrom()
 * transferTo();
 * 
 *   
 * 5; 分散(Scatter)与 聚集(Gather)\
 *   分散读取 (Scattering reads) 将通道中的数据分散到缓冲区中
 *   聚集写入(gethering writes)  将缓冲区的数据 聚集到通道
 *   
 * 6 字符集
 * 编码   字符串-字符数组  .
 * 解码   字符数组-字符串 
 */
public class TestChannel {
	
	@Test
	public void test6() throws CharacterCodingException {
		
		
		Charset cs1=Charset.forName("GBK");
		
		//获取编码器
		CharsetEncoder ce=cs1.newEncoder();
		//获取解码器
		CharsetDecoder cd=cs1.newDecoder();
		
		
		CharBuffer cbuf=CharBuffer.allocate(1024);
		cbuf.put("测试编码器");
		cbuf.flip();
		//编码
		ByteBuffer bBuf=ce.encode(cbuf);
		
		for(int i=0;i<10;i++) {
			System.err.println(bBuf.get());
		}
		bBuf.flip();
		//解码
		CharBuffer cBuf2=cd.decode(bBuf);
		System.err.println(cBuf2.toString());
	}
	
	@Test
	//字符集
 public void test5() {
	 Map<String ,Charset> map=	 Charset.availableCharsets();
	 
Set<Entry<String,Charset>>	set= map.entrySet();

  for (Entry<String, Charset> entry : set) {
	System.err.println(entry.getKey()+ "="+entry.getValue());
}
 }
	
	
	
	
	
	//分散聚集
	@Test
	public void test4() throws IOException {
		
		
		RandomAccessFile raf1=new RandomAccessFile("H:\\java\\nio\\1.txt", "rw");
		//获取通道
		FileChannel channel1=raf1.getChannel();
		
		//分配指定大小缓冲区
		ByteBuffer bf1=ByteBuffer.allocate(100);
		ByteBuffer bf2=ByteBuffer.allocate(1024);
		ByteBuffer[] bfs= {bf1,bf2};
		
		//分散读取
		channel1.read(bfs);
		
//		for (ByteBuffer byteBuffer : bfs) {
//			byteBuffer.flip();
//			
//		}
//		System.err.println(new String(bfs[0].array(), 0,bfs[0].limit()));
	
	RandomAccessFile raf2=new RandomAccessFile("H:\\java\\nio\\2.txt", "rw");
  FileChannel channel2=raf2.getChannel();
  channel2.write(bfs);
  channel1.close();
  channel2.close();
	}
	
	
	
	@Test
	public void test3() throws IOException {
		
		FileChannel inChannel=FileChannel.open(Paths.get("H:\\java\\nio\\Java NIO.pdf"), StandardOpenOption.READ);
		FileChannel outChannel=FileChannel.open(Paths.get("H:\\java\\nio\\Java NIO2.pdf"), StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		
	inChannel.transferTo(0,inChannel.size(),outChannel);
	inChannel.close();
	outChannel.close();
	}
	
	
	//1.利用通道完成 文件的复制.
	@Test
	public void test1()  {
		
		FileInputStream fis=null;
		FileOutputStream fos=null;
		FileChannel inChannel=null;
		FileChannel  outChannel=null;
		try {
			fis = new FileInputStream("H:\\test\\1\\1.png");
			 fos=new FileOutputStream("H:\\test\\1\\2.png");
			
			// 获取通道
				 inChannel=fis.getChannel();
			  outChannel=fos.getChannel();
				
				//分配指定大小的缓冲区.
				
				ByteBuffer buf=ByteBuffer.allocate(1024);
				
				//将通道中的数据 存入缓冲区.
				while(inChannel.read(buf)!=-1) {
					//切换读写模式
					buf.flip();
					//通道中的数据 写入缓冲区
					outChannel.write(buf);
					
					buf.clear();
					
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(outChannel!=null) {
				try {
					outChannel.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(inChannel !=null) {
				try {
					inChannel.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(fos!=null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			if(fis!=null) {
				
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		

	}
	
	//是用直接缓冲区完成文件的复制(内存映射文件)
	
	@Test
	public  void  test2() throws IOException {
		
		FileChannel inChannel=	FileChannel.open(Paths.get("H:\\test\\1\\1.png"), StandardOpenOption.READ);
		FileChannel outChannel=FileChannel.open(Paths.get("H:\\test\\1\\3.png"), StandardOpenOption.WRITE,StandardOpenOption.READ,StandardOpenOption.CREATE_NEW);
	//内存映射文件	
	MappedByteBuffer inMapBuf=	inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
	MappedByteBuffer outMapBuf=	outChannel.map(MapMode.READ_WRITE, 0, inChannel.size());
	
	//直接对缓存区进行数据的读写操作
	byte[] dst=new  byte[inMapBuf.limit()];
	inMapBuf.get(dst);
	outMapBuf.put(dst);
	inChannel.close();
	outChannel.close();
	}

}
