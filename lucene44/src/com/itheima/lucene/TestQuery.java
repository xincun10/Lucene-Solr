package com.itheima.lucene;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.itheima.utils.LuceneUtils;;

/**
 * 
 * indexSearcher.searcher(Query )
 * 
 * Query 是一个查询，条件，不同的子类相当于不同的查询规则
 * 
 * 我们可以扩展..
 * 
 * @author Administrator
 *
 */
public class TestQuery {
		
		public void printResult(IndexSearcher indexSearcher, Query query) throws Exception
		{
			TopDocs topDocs = indexSearcher.search(query, 10);
			System.out.println("总记录数==="+topDocs.totalHits);
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				// scoreDoc.doc属性就是document对象的id
				Document document=indexSearcher.doc(scoreDoc.doc);
				// 文件名称
				String fileName = document.get("fileName");
				System.out.println(fileName);
				// 文件内容
				String fileContent = document.get("fileContent");
				System.out.println(fileContent);
				// 文件大小
				String fileSize = document.get("fileSize");
				System.out.println(fileSize);
				// 文件路径
				String filePath = document.get("filePath");
				System.out.println(filePath);
				System.out.println("--------------------------");
			}
		}
		
		public IndexSearcher getIndexSearcher() throws Exception
		{
			Directory directory = FSDirectory.open(new File("H:\\lucene\\temp\\index"));
			IndexReader indexReader = DirectoryReader.open(directory);
			return new IndexSearcher(indexReader);
		}
		
		/**
		 * 第一种查询：查询所有..
		 * @throws Exception
		 */
		@Test
		public void testMatchAllDocsQuery() throws Exception {
			
			IndexSearcher indexSearcher = getIndexSearcher();
			Query query=new MatchAllDocsQuery();
			printResult(indexSearcher, query);
			//关闭资源
			indexSearcher.getIndexReader().close();
		}
		
		/**
		 * 第二种查询，TermQuery 
		 * @throws Exception
		 */
		@Test
		public void testTermQuery() throws Exception
		{
			Query query = new TermQuery(new Term("fileName", "java"));
			IndexSearcher indexSearcher = getIndexSearcher();
			printResult(indexSearcher, query);
			indexSearcher.getIndexReader().close();
		}

		/**
		 * 第三种查询：范围查询，可以使用此查询来替代过滤器...
		 * @throws Exception
		 */
		@Test
		public void testNumericRangeQuery() throws Exception
		{
			//创建查询
			//参数：
			//1.域名
			//2.最小值
			//3.最大值
			//4.是否包含最小值
			//5.是否包含最大值
			Query query = NumericRangeQuery.newLongRange("fileSize", 100l, 200l, true, true);
			IndexSearcher indexSearcher = getIndexSearcher();
			printResult(indexSearcher, query);
			indexSearcher.getIndexReader().close();
		}
			
		/**
		 * 第四种查询：布尔查询
		 * @throws Exception
		 */
		@Test
		public void testBooleanQuery() throws Exception
		{
			BooleanQuery query=new BooleanQuery();
			//id 1~10
			Query query1=NumericRangeQuery.newIntRange("id", 1, 10, true, true);
			
			Query query2=NumericRangeQuery.newIntRange("id", 5, 15, true, true);
			
			//select * from table  where title=? or  content=?
			
			//组合查询条件
			//必须满足第一个条件...
			query.add(query1, Occur.MUST);
			//应该满足第二个条件
			query.add(query2, Occur.SHOULD);
			
			IndexSearcher indexSearcher = getIndexSearcher();
			printResult(indexSearcher, query);
			indexSearcher.getIndexReader().close();
		}
		
		/**
		 * 第五种查询：QueryParser
		 * @throws Exception
		 */
		@Test
		public void testQueryParser() throws Exception
		{
			//创建queryparser对象
			//第一个参数默认搜索的域
			//第二个参数就是分析器对象
			QueryParser queryParser = new QueryParser(LuceneUtils.getMatchVersion(), "fileName", new IKAnalyzer());
			Query query = queryParser.parse("+filename:apache +content:apache");
//			Query query = queryParser.parse("filename:apache AND content:apache")
			IndexSearcher indexSearcher = getIndexSearcher();
			printResult(indexSearcher, query);
			indexSearcher.getIndexReader().close();
		}
		
		/**
		 * 第六种查询：指定多个默认域查询
		 * @throws Exception
		 */
		@Test
		public void testMulitFieldQueryParser() throws Exception
		{
			//可以指定默认搜索的域是多个
			String fields []={"fileName", "fileContent"};
			QueryParser queryParser=new MultiFieldQueryParser(LuceneUtils.getMatchVersion(),fields,LuceneUtils.getAnalyzer());
			//查询文件名和内容都有apache的文件
			Query query = queryParser.parse("apache");
			
			IndexSearcher indexSearcher = getIndexSearcher();
			printResult(indexSearcher, query);
			indexSearcher.getIndexReader().close();
		}
		
		//第五种查询：通配符。。。
		//?代表单个任意字符，* 代表多个任意字符...
		//Query query=new WildcardQuery(new Term("title", "luce*"));
		
		
		//第六种查询：模糊查询..。。。
		//author String 
		/**
		 * 1:需要根据查询的条件
		 * 2:最大可编辑数  取值范围0，1 ,2
		 * 允许我的查询条件的值，可以错误几个字符...
		 */
		//Query query=new FuzzyQuery(new Term("author", "爱新觉罗杜小"),1);
}
