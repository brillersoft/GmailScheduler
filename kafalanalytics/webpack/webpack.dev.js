var webpackMerge = require('webpack-merge');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var commonConfig = require('./webpack.common.js');
var helpers = require('./helpers');

module.exports = webpackMerge(commonConfig, {
  devtool: 'inline-source-map',

  output: {
    path: helpers.root('src/main/webapp' , 'build'),
    publicPath: './src/main/webapp/build/',
    filename: 'assets/js/[name].js',
    chunkFilename: 'assets/js/[id].chunk.js'
  },

  plugins: [
    new ExtractTextPlugin({ // define where to save the file
      filename: 'assets/css/[name].bundle.css',
      allChunks: true,
    })
  ],

  devServer: {
    historyApiFallback: true,
    stats: 'minimal'
  }
});
