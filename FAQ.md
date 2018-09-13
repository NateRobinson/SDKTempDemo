# FAQ List

### 1. Do I have to set the CustomTypeAdapter when initializing ABCoreKitClient ？

Not required, but in order to automatically identify our CustomTypeAdapter in schema.json, we have to do this in app.build Set the following code in the file so that generate code plugin can automatically recognize the corresponding CustomType:
 
```
apollo {
  customTypeMapping['DateTime'] = "java.util.Date"
}
```

### 2. Why is there no getXX, setXX method for the properties of the Bean object that I came up with generate code plugin?

Apollo generate code has a custom configuration, and we also need to add the following code to explicitly tell generate code plugin, otherwise the properties of the Bean object he generated would exist as public and would not exist Get, set method

```
apollo {
  useJavaBeansSemanticNaming = true
}
```

### 3. Are there any special requirements for the paths that the schema.json and .graphql files hold?

Just put it under the `/main/graphql/` path, and you can add more subdirectories below, just make sure the corresponding `schema.json` and `.graphql` files are in the same directory.

Such as `/main/graphql/a/b/c/A.graphql` will be generate code automatically generated plugin package path is: `a.b.c`, and name is `A.java`, generate code rule is according to the same directory `schema.json` .

- **Note 1**： if you change the path that the `schema.json` and `.graphql` files hold, you need to perform a `build clean` operation, otherwise you might get an error.

- **Note 2**：at least one `.graphql` file exists in a folder that holds `schema.json`, otherwise you'll get build errors.

### 4. When you initialize ABCoreKitClient, you pass in a `ResponseFetcher` object. What's the use?

This is used to define different fetch rules, Apollo provides a total of five rules for our direct use, specific see `ApolloResponseFetchers.java`:

- CACHE_ONLY :

  > Only get data from cache
  
- NETWORK_ONLY : 
	
  > Only get data from net

- CACHE_FIRST :

  > Get the data from cache first, and then go to net if it is not available or fails

- NETWORK_FIRST :

  > First fetch data from net, no or fail to cache

- CACHE_AND_NETWORK :

  > Fetch data from cache and net at the same time, and the data will return multiple times to the View layer 

### 5. What are the requirements for the content format of the graphql file?

Usually, we will first in [OCAP Playground] (https://ocap.arcblock.io/) write and test we need to query, mutation, subscription business statement, make sure no problem, then copy the corresponding content to we created in the Project `.graphql`.

Here you fill in the `query`, `mutation`, `subscription` of `.graphql` file and need to be named, and the parameters need to be passed in real time rather than written down in the statement. See the following example:

**The wrong case :**

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

**The right way :** 

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

In the right way, the Apollo code generate plugin can correctly identify and generate the corresponding Java code


