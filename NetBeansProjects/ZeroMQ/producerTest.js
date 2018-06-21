/* 
 * ProducerTest
 */
var zmq = require('/home/chrispowell/npmProjects/zeromq/node_modules/zeromq')
  , sock = zmq.socket('push');

sock.bindSync('tcp://127.0.0.1:8777');
console.log('Producer bound to port 8777');

setInterval(function(){
  console.log('sending work');
  sock.send('some work');
}, 500); 