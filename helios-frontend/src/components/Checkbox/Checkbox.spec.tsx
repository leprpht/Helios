import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import Checkbox from './Checkbox';
import '@testing-library/jest-dom';

describe('Checkbox component', () => {
  test('renders checkbox input', () => {
    render(<Checkbox checked={false} onChange={() => {}} />);

    const input = screen.getByRole('checkbox');
    expect(input).toBeInTheDocument();
    expect(input).toHaveAttribute('type', 'checkbox');
    expect(input).not.toBeChecked();
  });

  test('reflects checked prop when true', () => {
    render(<Checkbox checked={true} onChange={() => {}} />);

    const input = screen.getByRole('checkbox');
    expect(input).toBeChecked();
  });

  test('calls onChange with true when clicked while unchecked', async () => {
    const event = userEvent.setup();
    const onChange = jest.fn();

    render(<Checkbox checked={false} onChange={onChange} />);

    const input = screen.getByRole('checkbox');
    await event.click(input);

    expect(onChange).toHaveBeenCalledTimes(1);
    expect(onChange).toHaveBeenCalledWith(true);
  });

  test('calls onChange with false when clicked while checked', async () => {
    const event = userEvent.setup();
    const onChange = jest.fn();

    render(<Checkbox checked={true} onChange={onChange} />);

    const input = screen.getByRole('checkbox');
    await event.click(input);

    expect(onChange).toHaveBeenCalledTimes(1);
    expect(onChange).toHaveBeenCalledWith(false);
  });
});
