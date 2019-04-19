package com.itheima.lucene;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class FirstLucene {

	// 建立索引
	@Test
	public void testIndex() throws Exception {
		// 第一步：创建一个java工程，并导入jar包。
		// 第二步：创建一个indexwriter对象。
		// 1）指定索引库的存放位置Directory对象
		// 2）指定一个分析器，对文档内容进行分析。
		Directory directory = FSDirectory.open(new File("H:\\lucene\\temp\\index"));
		Version matchVersion = Version.LUCENE_44;// 当前Lucene版本
		Analyzer analyzer = new StandardAnalyzer(matchVersion);
		IndexWriterConfig config = new IndexWriterConfig(matchVersion, analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);

		File f = new File("H:\\lucene\\searchsource");// 原始文档存放目录
		File[] listFiles = f.listFiles();
		for (File file : listFiles) {
			// 第二步：创建document对象。
			Document document = new Document();
			// 第三步：创建field对象，将field添加到document对象中。
			// 文件名称
			String file_name = file.getName();
			// 域Field
			Field fileNameField = new TextField("fileName", file_name, Store.YES);
			// 文件大小
			long file_size = FileUtils.sizeOf(file);
			Field fileSizeField = new LongField("fileSize", file_size, Store.YES);
			// 文件路径
			String file_path = file.getPath();
			Field filePathField = new StoredField("filePath", file_path);
			// 文件内容
			String file_content = FileUtils.readFileToString(file);
			Field fileContentField = new TextField("fileContent", file_content, Store.YES);

			document.add(fileNameField);
			document.add(fileSizeField);
			document.add(filePathField);
			document.add(fileContentField);
			// 第四步：使用indexwriter对象将document对象写入索引库，此过程进行索引创建。并将索引和document对象写入索引库。
			indexWriter.addDocument(document);
		}

		// 第五步：关闭IndexWriter对象。
		indexWriter.close();

	}

	// 索引查询
	@Test
	public void testSearch() throws Exception {
		// 第一步：创建一个Directory对象，也就是索引库存放的位置。
		Directory directory = FSDirectory.open(new File("H:\\lucene\\temp\\index"));
		// 第二步：创建一个indexReader对象，需要指定Directory对象。
		IndexReader indexReader = DirectoryReader.open(directory);
		// 第三步：创建一个indexsearcher对象，需要指定IndexReader对象
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		// 第四步：创建一个TermQuery对象，指定查询的域和查询的关键词。
		Query query = new TermQuery(new Term("fileName", "one.txt"));
		// 第五步：执行查询。
		// 第一个参数是查询对象，第二个参数是查询结果返回的最大值
		TopDocs topDocs = indexSearcher.search(query, 2);// 返回排名最前的2条数据
		// 查询结果的总条数
		System.out.println("查询结果的总条数：" + topDocs.totalHits);
		// 第六步：返回查询结果。遍历查询结果并输出。
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		// topDocs.scoreDocs存储了document对象的id
		for (ScoreDoc scoreDoc : scoreDocs) {
			// scoreDoc.doc属性就是document对象的id
			int docID = scoreDoc.doc;
			Document document = indexSearcher.doc(docID);// 根据文档ID返回Document
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
		// 第七步：关闭IndexReader对象
		indexReader.close();
	}

	// 查看标准分析器的分词效果
	@Test
	public void testTokenStream() throws Exception {
		Version matchVersion = Version.LUCENE_44;
		// 创建一个标准分析器对象
//		Analyzer analyzer = new StandardAnalyzer(matchVersion);
		Analyzer analyzer = new IKAnalyzer();
		// 获得tokenStream对象
		// 第一个参数：域名，可以随便给一个
		// 第二个参数：要分析的文本内容
		TokenStream tokenStream = analyzer.tokenStream("test",
				"如果喜欢花，就去做园丁，做自己喜欢的事，没有比较，只有爱。");
		// 添加一个引用，可以获得每个关键词
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		// 添加一个偏移量的引用，记录了关键词的开始位置以及结束位置
		OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
		// 将指针调整到列表的头部
		tokenStream.reset();
		// 遍历关键词列表，通过incrementToken方法判断列表是否结束
		while (tokenStream.incrementToken()) {
			// 关键词的起始位置
			System.out.println("start->" + offsetAttribute.startOffset());
			// 取关键词
			System.out.println(charTermAttribute);
			// 结束位置
			System.out.println("end->" + offsetAttribute.endOffset());
		}
		tokenStream.close();
	}

}
