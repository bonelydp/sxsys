const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  devServer:{
    port: 8080, // 这里设置固定端口，比如 8080
    host: 'localhost',
    proxy:{
      '/api':{
        target:'http://localhost:8086',
        changeOrigin:true,
        pathRewrite: {                    // 路径重写规则
          '^/api': ''                   // 将 /api 替换为空字符串
        },
        ws:true
      }
    }
  },
  configureWebpack: {
    resolve: {
      fallback: {
        "assert": require.resolve("assert/"),
        "path": require.resolve("path-browserify")
      }
    }
  }
})
