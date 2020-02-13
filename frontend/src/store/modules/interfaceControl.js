import { make } from "vuex-pathify";

const state = () => ({
  groupDialog: false,
  fileUploadDialog: false,
  zoomImageDialog: false,
});

const mutations = make.mutations(state);

const actions = {
  ...make.actions("GroupDialog"),
  ...make.actions("FileUploadDialog"),
  ...make.actions("ZoomImageDialog"),
};

export const interfaceControl = {
  namespaced: true,
  state,
  mutations,
  actions
};
