import axios from 'axios';
import type { Collectible } from '../types/Collectible';

export class Api {
    static url = 'http://localhost:8080/api';
    
    async fetchCollectibles(token: string): Promise<Collectible[]> {
        return axios.get(`${Api.url}/collectibles/data`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }).then(res => res.data);
    }
    
    async fetchCollectiblesByType(token: string, type: string): Promise<Collectible[]> {
        return axios.get(`${Api.url}/collectibles/data/${type}`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }).then(res => res.data);
    }
    
    async updateCollectibles(token: string, collectibles: boolean[]): Promise<void> {
        return axios.post(`${Api.url}/collectibles/data`, { collectibles }, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }).then(() => {});
    }
}
