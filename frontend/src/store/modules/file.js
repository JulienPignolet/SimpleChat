import * as types from "../types";
import { make } from "vuex-pathify";
import axios from "axios";
import * as constants from "../../constants/constants";
import { File } from "../../models/File";

const state = () => ({
  file: {},
  newFiles: [],
  fileList: []
});

const getters = {
  ...make.getters(state),
  files: state => {
    return state.fileList;
  },
};

const mutations = make.mutations(state)

const actions = {
  ...make.actions(state),
  async [types.sendFile]({ dispatch, rootState }, file) {
    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    axios.defaults.headers.post['Content-Type'] = 'multipart/form-data';
    axios.post(constants.API_URL + "api/fileUpload/uploadFile", file)
      .then(function () {
        dispatch((`interfaceControl/${types.setFileUploadDialog}`), false, { root: true })
        dispatch((`chat/${types.getLiveMessages}`), null, { root: true })
      })
  },
  async [types.sendMultipleFiles]({ dispatch, rootState }, files) {
    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    axios.defaults.headers.post['Content-Type'] = 'multipart/form-data';
    axios.post(constants.API_URL + "api/fileUpload/uploadMultipleFiles", files)
      .then(function () {
        dispatch((`interfaceControl/${types.setFileUploadDialog}`), false, { root: true })
        dispatch((`chat/${types.getLiveMessages}`), null, { root: true })
      })
  },
  async [types.getFileData]({ rootState, state }, fileId) {
    axios.defaults.headers.post['user_key'] = rootState.user.user.token;
    axios.defaults.headers.post['Content-Type'] = 'multipart/form-data';
    axios.get(constants.API_URL + `api/fileUpload/getFileData/${fileId}`)
      .then(function (response) {
        state.fileList = [
          ...state.fileList.filter(file => file.id !== response.data.fileId),
          new File(response.data.fileId, response.data.fileName, response.data.fileType, response.data.fileSize)
        ];
      })
  }
};

export const file = {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
