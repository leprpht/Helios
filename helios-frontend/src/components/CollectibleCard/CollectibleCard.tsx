import type { Collectible } from '../../types/Collectible';
import Checkbox from '../Checkbox/Checkbox';
import styles from './CollectibleCard.module.css';

type Props = Collectible & {
  onToggleFound: (newValue: boolean) => void;
};

export default function CollectibleCard({
  type,
  title,
  location,
  description,
  isFound,
  link,
  onToggleFound
}: Props) {

  return (
    <div className={styles.card}>
      <div className={styles.cardHeader}>
        <div className={styles.cardTitle}>
          <h2>{type}: {title}</h2>
        </div>
      </div>
      <div className={styles.cardBody}>
        <p className={styles.cardDescription}>{location}</p>
        <p className={styles.cardDescription}>{description}</p>
        <Checkbox checked={isFound} onChange={onToggleFound} />
        <a className={styles.cardLocation} href={link}>[LOCATION]</a>
      </div>
    </div>
  );
}
