export default function Login() {
  return (
    <>
      <div className='flex flex-col h-full overflow-auto'>
        <div className='min-h-[45vh]'>
          <div className='flex flex-col justify-center h-[40%] space-y-1'>
            <div className='f-c-c text-8xl font-bold tracking-widest'>
              LOOVIE
            </div>
            <div className='f-c-c text-xl font-semibold tracking-wider'>
              Movie Recommend
            </div>
          </div>
          <div className='f-c-c flex-col h-[40%] space-y-5'>
            <input
              type='text'
              className='w-[30%] h-1/5 rounded-2xl text-center'
            />
            <input
              type='password'
              className='w-[30%] h-1/5 rounded-2xl text-center'
            />
          </div>
          <div className='h-[20%] f-c-s'>
            <div className='w-[30%] h-1/3 f-b-c'>
              <div className='w-[45%] h-full bg-white rounded-2xl f-c-c text-lg'>
                SignUp
              </div>
              <div className='w-[45%] h-full bg-white rounded-2xl f-c-c text-lg'>
                Login
              </div>
            </div>
          </div>
        </div>
        <div className='min-h-[30vh]'></div>
      </div>
    </>
  );
}
