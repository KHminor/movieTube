import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

const Api = createApi({
  reducerPath: "Api",
  tagTypes: ["UserApi"],
  baseQuery: fetchBaseQuery({
    baseUrl: process.env.REACT_APP_API_URL,
  }),
  endpoints: (builder) => ({
    getUsersInfo: builder.query({
      query: () => "/users",
      providesTags: (result, error, arg) => {
        return [{ type: "UserApi" }];
      },
    }),
    putUsersInfo: builder.mutation({
      query: (data) => {
        return {
          url: `/users`,
          method: "PUT",
          body: data,
        };
      },
      invalidatesTags: (result, error, arg) => {
        return [{ type: "UserApi" }];
      },
    }),
  }),
});

export const { useGetUsersInfoQuery, usePutUsersInfoMutation } = Api;

export default Api;
