import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        traverseData: JSON.parse(sessionStorage.getItem('traverseData')) || null
    },
    mutations: {
        setTraverseData(state, data) {
            state.traverseData = data;
            sessionStorage.setItem('traverseData', JSON.stringify(data)); // 存储到 sessionStorage
        }
    },
    actions: {
        updateTraverseData({ commit }, data) {
            commit('setTraverseData', data);
        }
    },
    getters: {
        getTraverseData: (state) => state.traverseData
    }
});
