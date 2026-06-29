import { createStore } from 'vuex';

export default createStore({
  state: {
    isCollapse: false,
  },
  mutations: {
    toggleSidebar(state, value) {
      state.isCollapse = value;
    },
  },
  actions: {

  },
  modules: {
    
  }
});