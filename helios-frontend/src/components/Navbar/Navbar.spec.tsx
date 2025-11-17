import { render, screen } from '@testing-library/react';
import Navbar from './Navbar';
import '@testing-library/jest-dom';

describe('Navbar component', () => {
  test('renders logo', () => {
    render(<Navbar />);
    
    const logo = screen.getByAltText('Logo');
    expect(logo).toBeInTheDocument();
  });

  test('renders header text', () => {
    render(<Navbar />);
    
    const header = screen.getByText('HELIOS');
    expect(header).toBeInTheDocument();
  });

  test('renders all navigation links', () => {
    render(<Navbar />);
    
    const links = ['Home', 'Logs', 'Ciphers', 'Biomes'];
    
    links.forEach((text) => {
      const link = screen.getByText(text);
      expect(link).toBeInTheDocument();
      expect(link.tagName).toBe('A');
    });
  });
});
