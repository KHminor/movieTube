/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./public/**/*.html', './src/**/*.{js,jsx,ts,tsx}'],
  theme: {
    extend: {
      backgroundImage: {
        'back-image': "url('./assets/images/sunset.jpeg')",
      },
    },
  },
  plugins: [
    ({ addUtilities }) => {
      addUtilities({
        '.f-c-c': {
          '@apply flex justify-center items-center': '',
        },
        '.f-b-c': {
          '@apply flex justify-between items-center': '',
        },
        '.f-s-c': {
          '@apply flex justify-start items-center': '',
        },
        '.h-ani-200': {
          '@apply cursor-pointer transition-all duration-300 hover:scale-105':
            '',
        },
      });
    },
  ],
};
