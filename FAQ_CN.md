# FAQ 列表

### 1. 为什么我直接 implementation 提示找不到这个仓库？

编译错误信息：

```
Unable to resolve dependency for ':app@debug/compileClasspath': Could not resolve com.arcblock.corekit:absdkcorekit:x.x.x.
```

目前我们的 sdk 还没有完全对外开放，所以只有在 white ip list 里面的网络环境下才能成功 implementation

### 2. 一定需要在初始化 ABCoreKitClient 的时候设置 CustomTypeAdapter 吗？

不是必须的 ，但是为了能自动识别我们 schema.json 里面的 CustomTypeAdapter ，我们必须要在 app.build
 文件中设置好下列代码, 这样 gen code plugin 才能自动的识别对应的 CustomType：
 
```
apollo {
  customTypeMapping['DateTime'] = "java.util.Date"
}
```

### 3. 为什么我 gen 出来的 Bean 对象中的属性没有对应的 getXX，setXX 方法?

apollo gen code 有个 custom 的配置，我们同样需要添加下列代码显式的告诉 gen code plugin, 不然他生成的 Bean 对象的属性会以 public 的形式存在，不会有
get，set 方法

```
apollo {
  useJavaBeansSemanticNaming = true
}
```

### 4. schema.json 和 .graphql 文件存放的路径有特殊要求吗？

只要将其放到 /main/graphql/ 这个路径下即可，在下面可以再添加更多级的子目录，只要保证对应的 schema.json 和 .graphql 文件在同级目录下即可。
比如 /main/graphql/a/b/c/A.graphl 会被 gen code plugin 自动生成包路径为： a.b.c ,名称为 A.java 的文，gen code 规则是根据同路径下的 schema.json 文件。

- 注意点1：如果更改了 schema.json 和 .graphql 文件存放的路径，需要执行一下 clean 操作，不然可能会有报错。
- 注意点2：一个存放了 schema.json 的文件夹内至少存在一个 .graphql 文件，否则会引起 build 错误

### 5. 初始化 ABCoreKitClient 的时候，会传一个 ResponseFetcher 对象进去，这个用处是什么？

这个是用来定义不同的 fetch 规则的，apollo 一共提供了 5 种规则供我们直接使用，具体见 ApolloResponseFetchers.java 文件：

- CACHE_ONLY  只从 cache 取数据
- NETWORK_ONLY  只从 net 取数据
- CACHE_FIRST  先从 cache 取数据，没有或者失败了再去 net 取
- NETWORK_FIRST  先从 net 取数据，没有或者失败了再去 cache 取
- CACHE_AND_NETWORK  同时去 cache 和 net 取数据，这里会多次返回数据给 View 层

### 6. .graphql 文件内容格式的要求？

通常我们会先在 [OCAP Playground](https://ocap.arcblock.io/) 里面编写和检验我们需要的 query, mutation, subscription 业务语句，确认没有问题之后，再复制对应内容到我们在 Project 中创建的 .graphql 文件中。

但是这里填入 .graphql 文件的 query, mutation, subscription 需要具名的，并且参数需要实时传入而非写死在语句中，看下面的示例：

**错误例子：**

```
{
blocksByHeight(fromHeight: 1, paging: { size: 2 }){
  data {
    height
    hash
    }
  }
}
```

**正确方式：** 

```
query getBlocksByHeight($from:Int!, $paging:PageInput){
  blocksByHeight(fromHeight: $from, paging: $paging) {
    data {
      height
      hash
    }
  }
}
```

使用正确的方式，apollo code gen plugin 才可以正确的识别并生成对应 java 代码


