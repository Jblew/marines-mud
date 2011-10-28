var decaf;
window.onload = function() {
    var my_element = document.querySelector('#mud');
    var s;
    if (navigator.plugins["Shockwave Flash"] || navigator.plugins["Shockwave Flash 2.0"]) {
        s = 'flash';
    } else if ('WebSocket' in window) {
        s = 'websocket';
    } else {
        document.write('Nie masz flasha ani websockets.');
        return;
    }
    var options = {
        set_interface: {
            container: my_element,
            start_full: false
        },
        set_socket: {
            swf: '/resources/decafmud/src/flash/DecafMUDFlashSocket.swf',
            wspath: 'menu',
            wsport: 9000,
            policyport: 843
        },
        host: undefined,
        port: 9000,
        socket: s,
        encoding: 'utf8',
        language: 'en'
    };
    decaf = new DecafMUD(options);
};