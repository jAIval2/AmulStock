# AmulStock

A comprehensive full-stack application for tracking and managing Amul product stock availability with real-time notifications. The system consists of a Spring Boot backend with scheduled stock monitoring and a Next.js frontend for user interaction.

## 🚀 Features

- **Real-time Stock Monitoring**: Automated polling of Amul product inventory every 10 minutes
- **Smart Notifications**: Firebase-based notification system for stock availability alerts
- **User Cart Integration**: Track products in user carts and notify when items come back in stock
- **Modern Frontend**: Next.js 15 with TypeScript, Tailwind CSS, and Radix UI components
- **Firebase Integration**: Firestore database for data persistence and user management
- **Secure Authentication**: Spring Security with configurable user credentials
- **RESTful APIs**: Clean API endpoints for stock data retrieval
- **Responsive Design**: Mobile-first design with modern UI components
- **AI Integration**: Google AI integration with Genkit for enhanced features

## 🏗️ Architecture

### Backend (Spring Boot)
- **Framework**: Spring Boot 3.5.3 with Java 17
- **Database**: Firebase Firestore for NoSQL data storage
- **Security**: Spring Security with basic authentication
- **Scheduling**: Automated stock polling with retry mechanisms
- **Monitoring**: Spring Boot Actuator for application health monitoring
- **Reactive Programming**: WebFlux for non-blocking API calls

### Frontend (Next.js)
- **Framework**: Next.js 15.3.3 with TypeScript
- **Styling**: Tailwind CSS with custom components
- **UI Components**: Radix UI primitives with custom styling
- **State Management**: React Context API for cart and authentication
- **Forms**: React Hook Form with Zod validation
- **Charts**: Recharts for data visualization
- **AI Features**: Google AI Genkit integration

## 📋 Prerequisites

### Backend Requirements
- **Java**: 17 or higher
- **Maven**: 3.8+
- **Firebase Project**: With Firestore enabled
- **Firebase Service Account**: JSON credentials file

### Frontend Requirements
- **Node.js**: 18+ 
- **npm/pnpm**: Latest version
- **Firebase Config**: Web app configuration

## 🛠️ Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd AmulStock
```

### 2. Backend Setup

#### Configure Firebase
1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com)
2. Enable Firestore Database
3. Generate a service account key:
   - Go to Project Settings → Service Accounts
   - Click "Generate new private key"
   - Save the JSON file as `amulstock-firebase-adminsdk-fbsvc-b21d63754a.json`
   - Place it in the project root directory

#### Configure Application Properties
Update `src/main/resources/application.properties`:
```properties
spring.application.name=AmulStock
spring.security.user.name=your-admin-username
spring.security.user.password=your-secure-password

# Firebase Configuration
firebase.credentials.path=amulstock-firebase-adminsdk-fbsvc-b21d63754a.json
firebase.project-id=your-firebase-project-id
```

#### Build and Run Backend
```bash
# Install dependencies and build
mvn clean install

# Run the application
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 3. Frontend Setup

#### Navigate to Frontend Directory
```bash
cd frontend
```

#### Install Dependencies
```bash
# Using npm
npm install

# Using pnpm (recommended)
pnpm install
```

#### Configure Firebase
Create `src/lib/firebase.ts` with your Firebase web configuration:
```typescript
import { initializeApp } from 'firebase/app';
import { getFirestore } from 'firebase/firestore';
import { getAuth } from 'firebase/auth';

const firebaseConfig = {
  apiKey: "your-api-key",
  authDomain: "your-project.firebaseapp.com",
  projectId: "your-project-id",
  storageBucket: "your-project.appspot.com",
  messagingSenderId: "123456789",
  appId: "your-app-id"
};

const app = initializeApp(firebaseConfig);
export const db = getFirestore(app);
export const auth = getAuth(app);
```

#### Run Frontend Development Server
```bash
# Using npm
npm run dev

# Using pnpm
pnpm dev
```

The frontend will start on `http://localhost:9002`

## 🔧 Configuration

### Environment Variables

#### Backend (.env or application.properties)
```properties
# Security
spring.security.user.name=admin
spring.security.user.password=secure-password

# Firebase
firebase.credentials.path=path/to/service-account.json
firebase.project-id=your-firebase-project-id

# Server Configuration
server.port=8080
```

#### Frontend (.env.local)
```bash
NEXT_PUBLIC_FIREBASE_API_KEY=your-api-key
NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN=your-project.firebaseapp.com
NEXT_PUBLIC_FIREBASE_PROJECT_ID=your-project-id
NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET=your-project.appspot.com
NEXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID=123456789
NEXT_PUBLIC_FIREBASE_APP_ID=your-app-id

# Backend API URL
NEXT_PUBLIC_API_URL=http://localhost:8080
```

## 📚 API Documentation

### Stock Endpoints

#### Get Amul Stock Data
```http
GET /api/stock/amul
Authorization: Basic <base64-encoded-credentials>
```

**Response:**
```json
{
  "data": [
    {
      "_id": "product-id",
      "name": "Product Name",
      "price": 100,
      "inventory_quantity": 50,
      "available": true,
      "sku": "PRODUCT-SKU",
      "brand": "Amul"
    }
  ]
}
```

#### Get Formatted Product Data
```http
GET /hehe
Authorization: Basic <base64-encoded-credentials>
```

**Response:**
```json
[
  {
    "id": "product-id",
    "name": "Product Name",
    "price": 100,
    "quantity": 50,
    "available": true,
    "sku": "PRODUCT-SKU"
  }
]
```

### Authentication
All API endpoints require Basic Authentication:
```bash
curl -u username:password http://localhost:8080/api/stock/amul
```

## 🔄 Automated Features

### Stock Monitoring
- **Frequency**: Every 10 minutes (600,000 ms)
- **Retry Logic**: 3 attempts with 2-second delays
- **Error Handling**: Comprehensive logging and graceful degradation
- **Data Source**: Amul API with Gujarat store focus

### Notification System
- **Trigger**: When out-of-stock products become available
- **Target Users**: Users with the product in their cart
- **Delivery**: Firebase Cloud Messaging (FCM) - ready for implementation
- **Persistence**: Firestore for user cart and notification preferences

## 🗄️ Database Schema

### Firestore Collections

#### `carts`
```json
{
  "userId": "user-123",
  "items": [
    {
      "productId": "product-456",
      "quantity": 2,
      "addedAt": "2025-01-17T10:30:00Z"
    }
  ],
  "updatedAt": "2025-01-17T10:30:00Z"
}
```

#### `stock_data` (managed by StockRepository)
```json
{
  "productId": "product-456",
  "name": "Product Name",
  "price": 100,
  "inventory_quantity": 50,
  "available": true,
  "lastUpdated": "2025-01-17T10:30:00Z"
}
```

## 🚀 Deployment

### Backend Deployment

#### Using Docker
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/AmulStock-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

#### Build and Deploy
```bash
# Build JAR
mvn clean package

# Build Docker image
docker build -t amulstock-backend .

# Run container
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod amulstock-backend
```

### Frontend Deployment

#### Using Vercel (Recommended)
```bash
# Install Vercel CLI
npm i -g vercel

# Deploy
vercel --prod
```

#### Using Docker
```dockerfile
FROM node:18-alpine
WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production
COPY . .
RUN npm run build
EXPOSE 3000
CMD ["npm", "start"]
```

### Firebase Hosting
```bash
# Install Firebase CLI
npm install -g firebase-tools

# Login and initialize
firebase login
firebase init hosting

# Deploy
firebase deploy
```

## 🧪 Testing

### Backend Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AmulStockApplicationTests

# Generate test coverage report
mvn jacoco:report
```

### Frontend Tests
```bash
# Run tests (when configured)
npm test

# Type checking
npm run typecheck

# Linting
npm run lint
```

## 📊 Monitoring & Logging

### Application Health
- **Endpoint**: `http://localhost:8080/actuator/health`
- **Metrics**: `http://localhost:8080/actuator/metrics`
- **Info**: `http://localhost:8080/actuator/info`

### Logging Configuration
The application uses SLF4J with Logback for comprehensive logging:
- **Stock polling**: Detailed logs for API calls and data processing
- **Notifications**: User notification tracking and error handling
- **Firebase**: Connection status and operation logs

## 🔒 Security Considerations

### Backend Security
- **Authentication**: Spring Security with configurable credentials
- **CORS**: Configure for production domains
- **Firebase**: Service account key security
- **API Rate Limiting**: Consider implementing for production

### Frontend Security
- **Environment Variables**: Never expose sensitive keys in client-side code
- **Firebase Rules**: Implement proper Firestore security rules
- **Authentication**: Implement user authentication for cart features

### Firestore Security Rules
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users can only access their own cart
    match /carts/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Stock data is read-only for authenticated users
    match /stock_data/{document} {
      allow read: if request.auth != null;
      allow write: if false; // Only backend can write
    }
  }
}
```

## 🤝 Contributing

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/amazing-feature`
3. **Commit changes**: `git commit -m 'Add amazing feature'`
4. **Push to branch**: `git push origin feature/amazing-feature`
5. **Open a Pull Request**

### Development Guidelines
- Follow Java coding standards for backend
- Use TypeScript strict mode for frontend
- Write comprehensive tests for new features
- Update documentation for API changes
- Follow conventional commit messages

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

### Common Issues

#### Backend Issues
- **Firebase Connection**: Ensure service account JSON is correctly placed and configured
- **Port Conflicts**: Change server port in `application.properties`
- **Memory Issues**: Increase JVM heap size: `-Xmx1024m`

#### Frontend Issues
- **Build Errors**: Clear node_modules and reinstall dependencies
- **Firebase Config**: Verify all environment variables are set
- **Port Issues**: Change port in `package.json` dev script

### Getting Help
- **Issues**: Create an issue on GitHub
- **Documentation**: Check the inline code documentation
- **Logs**: Check application logs for detailed error information

## 🔄 Changelog

### Version 0.0.1-SNAPSHOT (Current)
- ✅ Initial project setup with Spring Boot and Next.js
- ✅ Firebase Firestore integration
- ✅ Automated stock monitoring system
- ✅ Basic notification framework
- ✅ RESTful API endpoints
- ✅ Modern frontend with Tailwind CSS
- ✅ User cart management
- ✅ Authentication system
- 🔄 FCM notification implementation (in progress)
- 🔄 Advanced filtering and search (planned)
- 🔄 Analytics dashboard (planned)

---

**Last Updated**: January 17, 2025  
**Version**: 0.0.1-SNAPSHOT  
**Maintainer**: AmulStock Development Team
