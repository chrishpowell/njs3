/* 
 * subscriberTest
 */
var zmq = require('/home/chrispowell/npmProjects/zeromq/node_modules/zeromq')
  , sock = zmq.socket('sub');

sock.connect('tcp://127.0.0.1:8777');
sock.subscribe('kitty cats');
console.log('Subscriber connected to port 8777');

sock.on('message', function(topic, message) {
  console.log('received a message related to:', topic, 'containing message:', message);
});