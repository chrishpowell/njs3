/* 
 * webpack config
 */
const path = require('path');
module.exports = {
    entry: {
        first: './components/comp-1/Comp1.js',
        second: './components/comp-1/Comp2.js',
        fourth: './components/comp-1/Comp4.js',
        fifth: './components/comp-1/Comp5.js',
        sixth: './components/comp-1/CompFTest1.js',
        seventh: './components/comp-1/Comp6a.js',
        eighth: './components/comp-1/CompFTest2.js',
        ninth: './components/comp-1/ClassProp1.js',
        s1: './components/comp-2/CompStyle1.js',
        s2: './components/comp-2/CompStyle2.js',
        s3: './components/comp-2/CompStyle3.js',
        s4: './components/comp-2/CompStyle4.js',
        s5: './components/comp-2/CompStyle5.js',
        s6: './components/comp-2/CompStyle6.js',
        t6: './components/comp-3/CompStyleTest1.js',
        t7: './components/comp-3/CompStyleTest2.js',
        t8: './components/comp-3/CompStyleTest3.js'
    },
    output: {
        filename: './public/js/[name].main.js',
        path: path.resolve(__dirname, 'predikt'),
        publicPath: '/predikt/'
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/i,
                exclude: /node_modules/,
                use: {
                    loader: 'babel-loader',
                    options: {
                          "presets": ["@babel/preset-env", "@babel/preset-react"],
                          /* NB!  plugins order is important... */
                          "plugins": [["@babel/plugin-proposal-decorators",{"legacy":true}],"@babel/plugin-proposal-class-properties"]
                          /* "plugins": [require("@babel/plugin-proposal-class-properties")] (^^ Above will lose legacy: sometime soon) ^^) */
                    }
                }
            },
            {
                test: /\.css$/i,
                use: [
                    'style-loader',
                    'css-loader'
                ]
            },
            {
                test: /\.(png|svg|jpe?g|gif)$/i,
                use: [
                    'file-loader'
                ]
            },
            {
                test: /\.less$/i,
                use: [{
                    loader: 'style-loader' // creates style nodes from JS strings
                  }, {
                    loader: 'css-loader' // translates CSS into CommonJS
                  }, {
                    loader: 'less-loader' // compiles Less to CSS
                  }]
            },
            {
                test: /\.(woff(2)?|ttf|eot|svg)(\?v=\d+\.\d+\.\d+)?$/i,
                use: [{
                    loader: 'file-loader',
                    options: {
                        name: '[name].[ext]',
                        outputPath: 'fonts/'
                    }
                }]
            }
        ]
    }
};
