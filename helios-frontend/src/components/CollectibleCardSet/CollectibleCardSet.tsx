import { useState } from 'react';
import CollectibleCard from '../CollectibleCard/CollectibleCard';
import styles from './CollectibleCardSet.module.scss';

export default function CollectibleCardSet() {
  const [foundStates, setFoundStates] = useState([false, true]);
  
  const toggleFound = (index: number, value: boolean) => {
    setFoundStates(prev => {
      const newStates = [...prev];
      newStates[index] = value;
      return newStates;
    });
  };

  return (
    <div className={styles.cardSet}>
      <CollectibleCard
        type="LOG"
        title='001 - PLANET ATROPOS'
        location='Rare Side room if missed in the prologue.'
        description={'Scout Log: Atropos. Elapsed time... Thirty minutes since last crash. Whole areas of this forest are rearranging themselves like... a fluid puzzle after each of my... When- whenever I return. Per "Astra Protocol", I will not be recovered until I reach the broadcast signal. If you\'re hearing this, you are stuck here too.'}
        isFound={foundStates[0]}
        onToggleFound={(v) => toggleFound(0, v)}
        link='#' />
      <CollectibleCard
        type="LOG"
        title='002 - ARRIVAL'
        location='Main room after ship (must die once to spawn this log).'
        description={'My end is waiting at the beginning, abandoned like Helios. Last drive. Failed escape. Fatal crash. My memories have been rearranged into spiral patterns I cannot comprehend, dragging me into the deepest of the deep. I will go there now. As you will.'}
        isFound={foundStates[1]}
        onToggleFound={(v) => toggleFound(1, v)}
        link='#' />
    </div>);
}