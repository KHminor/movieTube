import { configureStore, createSlice } from "@reduxjs/toolkit";
import Api from "./api";

const loginStatus = createSlice({
  name: "loginStatus",
  initialState: false,
  reducers: {
    changeLoginStatus(state, action) {
      return (state = action.payload);
    },
  },
});

export const store = configureStore({
  reducer: {
    [Api.reducerPath]: Api.reducer,
    loginStatus: loginStatus.reducer,
  },
});

export const { changeLoginStatus } = loginStatus.actions;
