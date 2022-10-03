import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/test/';
const rank = {
    'PLAYER': {rank: "Игрок", prefix: "", color: "9e9e9e", value: 0},
    'VIP': {rank: "VIP", prefix: "[VIP] ", color: "00be00", value: 1},
    'PREMIUM': {rank: "Premium", prefix: "[Premium] ", color: "00dada", value: 2},
    'HOLY': {rank: "Holy", prefix: "[Holy] ", color: "ffba2d", value: 3},
    'IMMORTAL': {rank: "Immortal", prefix: "[Immortal] ", color: "e800d5", value: 4},

    'YOUTUBE': {rank: "YouTube", prefix: "[YouTube] ", color: "fe3f3f", value: 5},
    'BUILDER': {rank: "Билдер", prefix: "[Билдер] ", color: "009c00", value: 6},

    'ORGANIZER': {rank: "Организатор", prefix: "[Организатор] ", color: "00bebe", value: 7},
    'MODER': {rank: "Модератор", prefix: "[Модер] ", color: "1b00ff", value: 8},

    'SRBUILDER': {rank: "Проверенный Билдер", prefix: "[Пр.Билдер] ", color: "009c00", value: 9},
    'DEV': {rank: "Разработчик", prefix: "[Dev] ", color: "00bebe", value: 10},

    'WARDEN': {rank: "Проверенный модератор", prefix: "[Пр. Модер] ", color: "1b00ff", value: 11},
    'MAPLEAD': {rank: "Главный билдер", prefix: "[Гл. билдер] ", color: "009c00", value: 12},

    'CHIEF': {rank: "Главный модератор", prefix: "[Гл. модер] ", color: "1b00ff", value: 13},
    'ADMIN': {rank: "Главный админ", prefix: "[Гл. админ] ", color: "00bebe", value: 14}
};

class UserService {
    getRank(rankGet) {
        if (rankGet != null) {
            return rank[rankGet];
        } else {
            return null;
        }
    }

    like(userId) {
        return axios.get('/api/user/like/' + userId);
    }
    unlike(userId) {
        return axios.get('/api/user/unlike/' + userId);
    }

    getLikeOrNo(userId) {
        return axios.get('/api/user/likeorno/'+userId);
    }

    getLikes(userId) {
        return axios.get('/api/user/likes/' + userId);
    }

    getPublicContent() {
        return axios.get(API_URL + 'all');
    }

    getUserBoard() {
        return axios.get(API_URL + 'user', {headers: authHeader()});
    }

    getModeratorBoard() {
        return axios.get(API_URL + 'mod', {headers: authHeader()});
    }

    getAdminBoard() {
        return axios.get(API_URL + 'admin', {headers: authHeader()});
    }
}

export default new UserService();
