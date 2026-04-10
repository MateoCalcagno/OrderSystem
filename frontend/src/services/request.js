const request = async (promise) => {
  const res = await promise;
  return res.data;
};

export default request;