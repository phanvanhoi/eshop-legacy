module.exports = {
    devServer: {
        proxy: {
            "^/": {
              target: "http://localhost:8090"
            }
        },
        disableHostCheck: true
      }
}