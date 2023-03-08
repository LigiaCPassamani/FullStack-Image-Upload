# 💻 FullStack-Image-Upload
Full-stack upload of images.

## ⚙️ Project Characteristics:
  >- ## ⚙️ Back-end:
    > - Written in java.
    > - Has a model to save image url.
    > - The file service resizes the images to max of 600 kb and then saves it.
    > - The file service also generate a radom string to add to the image path, so images do not save on top of others.
  >- ## ⚙️ Front-end:
    > - Written in react and using boostrap-react.
    > - Has a form that get the image and send to the back-end.
    > - Has a bottun that returns the saved image.
    > - Using axios to interact with the back-end.
    
## 🛠️ Build instructions
### Prerequisites
> - Java 17
> - React-js latest (run npm install to get all dependencies and then npm start to run the app)
> - Maven latest
> - **Default port**: 8081
> - Database: PostgresSQL
