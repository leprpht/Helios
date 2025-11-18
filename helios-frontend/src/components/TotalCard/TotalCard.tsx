import styles from './TotalCard.module.css';

type TotalCardData = {
  percentage: number;
  total: string[];
  logs: string[];
  ciphers: string[];
};

const names = [
  'OVERGROWN RUINS',
  'CRIMSON WASTES',
  'DERILICT CITADEL',
  'ECHOING RUINS',
  'FRACTURED WASTES',
  'ABYSSAL SCAR',
];

export default function TotalCard({
  percentage,
  total,
  logs,
  ciphers,
}: TotalCardData) {
  return (
    <div className={styles.container}>
      <div className={styles.card}>
        <h4>Total</h4>
        <h5>{percentage}%</h5>
      </div>

      <div className={styles.card}>
        {names.map((name, i) => (
          <div className={styles.row} key={name}>
            <p>{name}: {total?.[i] ?? '-'}</p>
            <p className={styles.separator}>:</p>
            <p>{logs?.[i] ?? '-'}</p>
            <p>{ciphers?.[i] ?? '-'}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
