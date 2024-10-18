import axios from 'axios';

class AxiosSingleton {
    constructor() {
        if (!AxiosSingleton.instance) {
            this.axiosInstance = axios.create({
                proxy: {
                    host: '127.0.0.1',
                    port: 8090,
                },
            });

            // Optional: Add interceptors for requests or responses
            this.axiosInstance.interceptors.request.use(
                (config) => {
                    // Modify config before request is sent
                    return config;
                },
                (error) => {
                    // Handle request error
                    return Promise.reject(error);
                }
            );

            this.axiosInstance.interceptors.response.use(
                (response) => {
                    // Handle response data
                    return response;
                },
                (error) => {
                    // Handle response error
                    return Promise.reject(error);
                }
            );

            AxiosSingleton.instance = this;
        }

        return AxiosSingleton.instance;
    }

    getInstance() {
        console.log('this.axiosInstance', this.axiosInstance)
        return this.axiosInstance;
    }
}

const instance = new AxiosSingleton();
Object.freeze(instance);

export default instance.getInstance();