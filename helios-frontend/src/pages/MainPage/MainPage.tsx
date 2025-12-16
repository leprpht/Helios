import CollectibleCardSet from "../../components/CollectibleCardSet/CollectibleCardSet";
import TotalCard from "../../components/TotalCard/TotalCard";

export default function MainPage() {
  return (
    <>
      <TotalCard 
            percentage={33}
            total={['5/10', '8/12', '4/8', '6/9', '7/11', '3/7']}
            logs={['3/5', '5/6', '2/4', '4/5', '3/6', '1/3']}
            ciphers={['2/5', '3/6', '2/4', '2/4', '4/5', '2/4']}
          />
      <CollectibleCardSet />
    </>
  );
}