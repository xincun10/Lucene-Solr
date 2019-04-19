package com.itheima.utils;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * 假设的你几行代码可以完成某个功能，抽取成一个方法
 * 
 * 假设在某个业务逻辑层可以共用，往上抽取，
 * 
 * 假设在多个业务层可以共用，提炼成工具类。
 * 
 * 假设你的这个业务方法在多个系统需要被使用.. 发布成一个服务...
 * 
 * 
 * @author Administrator
 *
 */
public class LuceneUtils {
	
	private static Directory directory=null;
	
	private static IndexWriterConfig config=null;
	
	
	
	private static Version matchVersion=null;
	
	private static Analyzer analyzer=null;
	
	static{
		
		try {
			directory=FSDirectory.open(new File(Contants.INDEXURL));
			
			matchVersion=Version.LUCENE_44;
			
			analyzer=new StandardAnalyzer(matchVersion);
			
			config=new IndexWriterConfig(matchVersion, analyzer);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * 
	 * @return返回用于操作索引的对象...
	 * @throws IOException
	 */
	public static IndexWriter getIndexWriter() throws IOException{
		IndexWriter indexWriter=new IndexWriter(directory,config);
		return indexWriter;
	}
	/**
	 * 
	 * 返回用于读取索引的对象..
	 * @return
	 * @throws IOException
	 */
	public static IndexSearcher getIndexSearcher() throws IOException{
		
		IndexReader indexReader=DirectoryReader.open(directory);
		
		IndexSearcher indexSearcher=new IndexSearcher(indexReader);
		
		return indexSearcher;
		
	}
	
	/**
	 * 返回lucene 当前使用的版本信息...
	 * 
	 * @return
	 */
	public static Version getMatchVersion() {
		return matchVersion;
	}
	
	/**
	 * 
	 * 
	 * 返回lucene 使用的分词器...
	 * @return
	 */
	public static Analyzer getAnalyzer() {
		return analyzer;
	}

}
