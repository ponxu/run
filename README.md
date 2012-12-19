Run
=================
一款适合Java网站开发的框架, 告别SSH的臃肿! 
用Java开发网站一个Jar就够了!

成功示例:
[Blog4j简介][blog4j]

功能:

 1. IOC
 2. REST MVC
 3. Log
 4. DBUtils

IOC 使用
--------
IOC支持setter注入, 可通过xml方式和注解方式配置

**通过xml方式配置**

beans.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <!-- beans节点可配置属性scope: singleton|protype, 默认:singleton -->
    <beans>
        <!-- bean节点可配置属性:
            scope: singleton|protype, 默认:beans的scope
            id: 唯一标示符
            class: 类
        
            property节点可配属性:
            name: 属性名字, 必填
            ref: 引用另外一个bean, 注入到此属性
            value: 直接注入普通类型的值, int, float, String等
            若ref, value为空, 将查找ID为 属性name的bean注入
        -->
        <bean id="testPerson" class="com.ponxu.Person" />
    
        <bean id="myBean" class="com.ponxu.MyBean">
           <property name="id" value="1" />
            <property name="person" ref="testPerson" />
        </bean>
    </beans>

使用的java代码

    BeanFactory.loadXMLFile("beans.xml"); // 从类路径下加载
    MyBean b = BeanFactory.get("myBean")

**通过注解方式配置**

MyBean.java

    package com.ponxu.test;
    @Bean(scope = "protype", id = "MyTestBean")
    // scope: 默认singleton
    // id: 默认myBean
    public class MyBean {
	    // 注入普通类型属性
        @Inject(val = "my bean")
        private String name;
        // 注入其他bean
		// 如果val和ref都为空, 注入此属性名为id的bean
        @Inject(ref = "post2")
        private Post post;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Post getPost() {
            return post;
        }
        public void setPost(Post post) {
            this.post = post;
        }
    }

使用的java代码

    BeanFactory.loadPackage("com.ponxu.test"); // 加载此包及子包
    MyBean b = BeanFactory.get("MyTestBean"));

Log使用
-------

    package test;
    import org.junit.Test;
    import com.ponxu.blog4j.model.Post;
    import com.ponxu.run.lang.BeanUtils;
    import com.ponxu.run.log.Log;
    import com.ponxu.run.log.LogFactory;
    public class TestLog {
        private static final Log LOG = LogFactory.getLog();
        // private static final Log LOG = LogFactory.getLog(TestLog.class);
        @Test
        public void go() {
            LOG.trace("Test %s", "Main");
            LOG.debug("Test %s", "Main");
            LOG.info("INFO");
            LOG.error("Error!");
            LOG.fatal("Error!");
            Post post = new Post(); // Blog4j的Posty
            BeanUtils.setProperty(post, "title", 123);
            BeanUtils.setProperty(post, "id", "12");
            BeanUtils.setProperty(post, "id", 1);
        }
    }

默认使用System.out作为日志输出, 级别为debug
如需修改, 添加run.properties文件

    log=com.ponxu.run.log.loggers.JDKLog
    # com.ponxu.run.log.loggers.NoOpLog
    log_level=info
    # dubug, error, fatal

假如项目存在Log4j, 以Log4j的配置为准


DBUtils使用
-----------

    Connection conn = 获取数据库连接;
    // 更新操作
    DBUtils.execute(conn , "update poset set title=? where id=?", '我的标题', 100);
    
    // 查询操作
    List<Map<String, String>> list = DBUtils.query(conn , 
    	MapRowWrapper.getInstance(), 
    	"select * from post where title=?", 
    	'测试标题');
    	
    /*
    查询操作中, 有一个RowWrapper接口, 把每一行的ResultSet包装成一个对象返回
    自带
    MapRowWrapper: 把每一行组成一个map
    LongRowWrapper: 每一行为一个Long数据
    StringRowWrapper: 每一行为一个String数据
    */
    List<Post> list = DBUtils.query(conn , 
    	// 自定义RowWrapper
    	new RowWrapper<Post>() {
    		public Post wrap(ResultSet rs) throws SQLException {
    			Post post = new Post();
    			post.setId(rs.getInt("id"));
    			post.setTitle(rs.getString("title"));
    			return post;
    		}
    	}, 
    	"select * from post where title=?", 
    	'测试标题');
    	
    // 事务支持: 方式一
    DBUtils.beginTranscation(conn);
    // 你的操作..
    ...
    DBUtils.commit(conn);
    
    // 事务支持: 方式二
    DBUtils.executeTranscation(conn, new Atom(){
    	public void run(Connection conn2) {
    		DBUtils.execute(conn2, sql1);
    		DBUtils.execute(conn2, sql2);
    		DBUtils.execute(conn2, sql3);
    	}
    });


**[MVC 使用(点击阅读)][mvc]**

**[Run on github][git]**


  [blog4j]: http://blog4j.cloudfoundry.com/post/1
  [mvc]: http://blog4j.cloudfoundry.com/post/10
  [git]: https://github.com/ponxu/run