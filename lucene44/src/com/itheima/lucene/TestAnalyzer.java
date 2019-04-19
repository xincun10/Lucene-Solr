package com.itheima.lucene;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 
 * 测试分词器...
 * @author Administrator
 *
 */
public class TestAnalyzer {
	/**
	 * 
	 * 1、分词器的作用
	 *     在创建索引时会用到分词器，在使用字符串搜索时也会用到分词器，这两个地方要使用同一个分词器，否则可能会搜索不出结果。
      Analyzer（分词器）的作用是把一段文本中的词按规则取出所包含的所有词。对应的是Analyzer类，这是一个抽象类，切分词的具体规则是由子类实现的，所以对于不同的语言（规则），要用不同的分词器	
	 * @throws IOException 
	 * 
	 * 
	 */
	
	public static void main(String[] args) throws IOException {
		//单字分词
		
		//		Analyzer analyzer=new StandardAnalyzer(Version.LUCENE_44);
		
		
		//二分法分词...
//		Analyzer analyzer=new CJKAnalyzer(Version.LUCENE_44);
		
		//Analyzer 是一个抽象类，我们能不能改造它，来定义自己的分词规则..
		
		//有没有第三方已经实现好了。我们直接拿过来用，
		
		//google download  第三方 的一些开发工具包...
		
		//github  很多第三方的资料...
		
		//第三方的中文的分词器,庖丁分词器， 中文分词，特点：扩展新的词，自定义停用词...
		
		
		//2012FF_u1
		Analyzer analyzer=new IKAnalyzer();
		
		
		//自定义扩展词...
		
		String text="lucene 是传智播客的一个全文检索的高大上的工具包";
		
		
		testAnalzyer(analyzer, text);
	}
	
	
	
	public static void testAnalzyer(Analyzer analyzer,String text) throws IOException{
		
		System.out.println("当前使用的分词器：" + analyzer.getClass().getSimpleName());
		TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(text));
	    tokenStream.addAttribute(CharTermAttribute.class);
	    tokenStream.reset();
	    while (tokenStream.incrementToken()) {
	       CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
	          System.out.println(new String(charTermAttribute.toString()));
	   }

		
		
	}

}
