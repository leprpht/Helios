import axios from 'axios';
import type { Collectible } from '../types/Collectible';

const url = 'http://localhost:8080/api'

export function fetchCollectibles(token: string): Promise<Collectible[]> {
    return axios.get(`${url}/collectibles/data`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    }).then(res => res.data);
}

export function fetchCollectiblesByType(token: string, type: string): Promise<Collectible[]> {
    return axios.get(`${url}/collectibles/data/${type}`, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    }).then(res => res.data);
}

export function updateCollectibles(token: string, collectibles: boolean[]): Promise<void> {
    return axios.post(`${url}/collectibles/data`, { collectibles }, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    }).then(() => {});
}
