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