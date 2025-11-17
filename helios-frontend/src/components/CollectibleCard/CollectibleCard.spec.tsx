import { render, screen } from '@testing-library/react';
import CollectibleCard from './CollectibleCard';
import '@testing-library/jest-dom';

describe('CollectibleCard component', () => {
  const props = {
    type: 'type',
    title: 'title',
    location: 'location',
    description: 'description',
    isFound: false,
    link: 'link',
    onToggleFound: () => {},
  };

  test('renders title', () => {
    render(<CollectibleCard {...props} />);
    
    const title = screen.getByText('type: title');
    expect(title).toBeInTheDocument();
  });

  test('renders location', () => {
    render(<CollectibleCard {...props} />);
    
    const location = screen.getByText('location');
    expect(location).toBeInTheDocument();
  });
  
  test('renders description', () => {
    render(<CollectibleCard {...props} />);

    const description = screen.getByText('description');
    expect(description).toBeInTheDocument();
  });

  test('renders link', () => {
    render(<CollectibleCard {...props} />);
    
    const link = screen.getByRole('link', { name: '[LOCATION]' });
    expect(link).toBeInTheDocument();

    expect(link.getAttribute('href')).toBe(props.link);
  });
});
