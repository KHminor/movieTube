import Lottie from 'lottie-react';
import logo from './../../assets/icons/take.json';
export default function Navigator() {
  return (
    <>
      <div className='flex f-s-c h-full w-1/5'>
        <Lottie className='h-full h-ani-200 ' animationData={logo} loop />
      </div>
      <div className='f-c-c w-[55%] bg-black'>빈칸</div>
      <div className='flex justify-evenly items-center h-full w-[25%]'>
        <div className='h-ani-200'>링크1</div>
        <div className='h-ani-200'>링크2</div>
        <div className='h-ani-200'>링크3</div>
      </div>
    </>
  );
}
