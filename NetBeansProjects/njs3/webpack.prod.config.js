/* 
 * webpack config
 */
const path = require('path');
module.exports = {
    entry: {
        home: './components/comp-1/Comp1.js'
    },
    output: {
        filename: './public/js/main.js',
        path: path.resolve(__dirname, 'predikt'),
        publicPath: '/'
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                use: {
                    loader: 'babel-loader',
                    options: {
                          "presets":["es2015", "react"]
                    }
                },
                exclude: /node_modules/
            },
            {
                test: /\.css$/,
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
            }
        ]
    }
};
