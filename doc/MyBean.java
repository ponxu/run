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