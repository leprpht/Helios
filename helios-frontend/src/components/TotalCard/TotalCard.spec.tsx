import { render, screen } from '@testing-library/react';
import TotalCard from './TotalCard';
import '@testing-library/jest-dom';

describe('TotalCard component', () => {
  const data = {
    percentage: 33,
    total: ['5/10', '8/12', '4/8', '6/9', '7/11', '3/7'],
    logs: ['3/5', '5/6', '2/4', '4/5', '3/6', '1/3'],
    ciphers: ['2/5', '3/6', '2/4', '2/4', '4/5', '2/4'],
  };

  test('renders total percentage', () => {
    render(<TotalCard {...data} />);

    const percentage = screen.getByText(/33%/i);
    expect(percentage).toBeInTheDocument();
  });

  test('renders correct names', () => {
    render(<TotalCard {...data} />);

    const names = [
      'OVERGROWN RUINS',
      'CRIMSON WASTES',
      'DERILICT CITADEL',
      'ECHOING RUINS',
      'FRACTURED WASTES',
      'ABYSSAL SCAR',
    ];

    names.forEach(name => {
      expect(screen.getByText(new RegExp(name, 'i'))).toBeInTheDocument();
    });
  });

  test('renders totals, logs, and ciphers correctly', () => {
    render(<TotalCard {...data} />);

    const countInArray = (arr: string[], value: string) =>
      arr.reduce((acc, cur) => acc + (cur === value ? 1 : 0), 0);

    data.total.forEach(value => {
      const found = screen.getAllByText(new RegExp(value, 'i'));
      expect(found.length).toBeGreaterThanOrEqual(countInArray(data.total, value));
    });

    data.logs.forEach(value => {
      const found = screen.getAllByText(new RegExp(`^${value}$|${value}`, 'i'));
      expect(found.length).toBeGreaterThanOrEqual(countInArray(data.logs, value));
    });

    data.ciphers.forEach(value => {
      const found = screen.getAllByText(new RegExp(value.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'), 'i'));
      expect(found.length).toBeGreaterThanOrEqual(countInArray(data.ciphers, value));
    });
  });
});
