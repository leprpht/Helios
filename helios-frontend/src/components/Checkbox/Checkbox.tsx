import type { FC } from 'react';
import styles from './Checkbox.module.scss';

type Props = {
  checked: boolean;
  onChange: (checked: boolean) => void;
};

const Checkbox: FC<Props> = ({ checked, onChange }) => {
  return (
    <div>
        <label className={styles.checkboxWrapper}>
          <input
            type="checkbox"
            checked={checked}
            onChange={e => onChange(e.target.checked)}
          />
          <span className={styles.customBox}></span>
        </label>
    </div>
  );
};

export default Checkbox;