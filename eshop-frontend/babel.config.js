module.exports = {
  presets: [
    '@vue/cli-plugin-babel/preset'
  ],
  plugins: [
    [
      'module-resolver',
      {
        root: ['./src'], // Adjust the root according to your project structure
        alias: {
          '@': './src', // Maps '@' to the 'src' directory
        },
      },
    ],
  ],
}
