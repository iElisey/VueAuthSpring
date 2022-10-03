import axios from 'axios';

const URL = 'https://api.vimeworld.com';
class VimeService {
    getGuild(id) {
        return axios.get(URL+'/guild/get?id=' + id);
    }
    toISODate(seconds) {
        var date = new Date(seconds * 1000);
        var y = date.getFullYear()
        var m = date.getMonth() + 1;
        var d = date.getDate();
        var hours = date.getHours();
        var min = date.getMinutes();
        var sec = date.getSeconds();
        m = (m < 10) ? '0' + m : m;
        d = (d < 10) ? '0' + d : d;
        return [y, m, d].join('.')+ ' '+[hours,min,sec].join(':');
    }

    getLocaleNames() {
        return axios.get(URL+'/locale/en')
    }

}

export default new VimeService();