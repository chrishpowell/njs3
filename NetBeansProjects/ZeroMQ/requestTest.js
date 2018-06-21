/* 
 * requestTest
 */
var zmq = require('/home/chrispowell/npmProjects/zeromq/node_modules/zeromq'),
    sock = zmq.socket('req');

sock.on('message', function(data)
{
	console.log('got messsage: ' + data.toString('utf8'));
});

sock.connect('tcp://127.0.0.1:8777');
console.log('Requester connected to port 8777');

var test = { foo: 'bar' };
sock.send(JSON.stringify(test));