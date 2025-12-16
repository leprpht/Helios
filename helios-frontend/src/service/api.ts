import axios from 'axios';
import type { Collectible } from '../types/Collectible';

const url = 'http://localhost:8080/api'

export function fetchCollectibles(): Promise<Collectible[]> {
    return axios.get(`${url}/collectibles/data`).then(res => res.data);
}