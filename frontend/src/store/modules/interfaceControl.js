import { make } from "vuex-pathify";

const state = () => ({
  groupDialog: false,
  fileUploadDialog: false,
  newPollDialog: false,
  drawpad: false,
  zoomImageDialog: false,
});

const mutations = make.mutations(state);

const actions = {
  ...make.actions("GroupDialog"),
  ...make.actions("FileUploadDialog"),
  ...make.actions("NewPollDialog"),
  ...make.actions("Drawpad"),
  ...make.actions("ZoomImageDialog"),
};

export const interfaceControl = {
  namespaced: true,
  state,
  mutations,
  actions
};
