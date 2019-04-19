package com.itheima.utils;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;

import com.itheima.bean.Article;

/**
 * article 的转换类...
 * 
 * @author Administrator
 *
 */
public class ArticleUtils {

	/**
	 * 将article 转换成document
	 * 无非article 的值设置document里面去...
	 * 
	 * 
	 * @param article
	 * @return
	 */
	public static Document articleToDocument(Article article){
		
		Document document=new Document();
		
		IntField idfield=new IntField("id",article.getId(),Store.YES);
		
		StringField authorfield=new StringField("author", article.getAuthor(), Store.YES);
		StringField urlfield=new StringField("link", article.getLink(), Store.YES);
		TextField title=new TextField("title", article.getTitle(),Store.YES);
		
		//设置权重值，默认为1f..
		//title.setBoost(4f);
		
		TextField contentfield=new TextField("content", article.getContent(),Store.YES);
		
		document.add(idfield);
		document.add(authorfield);
		
		document.add(urlfield);
		document.add(title);
		document.add(contentfield);
		
		return document;
		
	}
}
