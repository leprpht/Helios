import styles from './Navbar.module.css';

export default function Navbar() {
  return (
    <nav className={styles.navbar}>
      <div>
        <img src="src/assets/helios.png" alt="Logo" className={styles.logo} />
        <h1 className={styles.header}>HELIOS</h1>
      </div>
      <div>
        <ul className={styles.navList}>
            <li className={styles.navItem}>
                <a href="#">Home</a>
            </li>
            <li className={styles.navItem}>
                <a href="#">Logs</a>
            </li>
            <li className={styles.navItem}>
                <a href="#">Ciphers</a>
            </li>
            <li className={styles.navItem}>
                <a href="#">Biomes</a>
            </li>
        </ul>
      </div>
    </nav>
  );
}
