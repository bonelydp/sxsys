// src/stores/blastStore.js
import { defineStore } from 'pinia';

export const useBlastStore = defineStore('blast', {
  state: () => ({
    blastResults: [],
    selectedSequence: null,
    formData:null
  }),
  actions: {
    setBlastResults(results) {
      this.blastResults = results;
    },
    setSelectedSequence(sequence) {
      this.selectedSequence = sequence;
    },
    setFormData(formData){
        this.formData = formData
    }
  },
});

