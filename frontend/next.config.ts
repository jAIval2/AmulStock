import type {NextConfig} from 'next';

const nextConfig: NextConfig = {
  output: 'export',  
  distDir: 'out',    
  trailingSlash: false,
  skipTrailingSlashRedirect: true,
  images: {
    unoptimized: true, 
    remotePatterns: [
      {
        protocol: 'https',
        hostname: 'placehold.co',
        port: '',
        pathname: '/**',
      },
      {
        protocol: 'https',
        hostname: 'shop.amul.com',
        port: '',
        pathname: '/s/62fa94df8c13af2e242eba16/**',
      },
    ],
  },
  typescript: {
    ignoreBuildErrors: true,
  },
  eslint: {
    ignoreDuringBuilds: true,
  },
};

export default nextConfig;
