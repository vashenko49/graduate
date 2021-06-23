const axios = require('axios');


for (let i = 0; i < 100; i++) {
    axios.get('http://localhost:8001/api/connect', {
        params: {
            link: 'ws://localhost:8080/streaming'
        }
    })
        .then(res => {
            console.log('ok');
        })
}