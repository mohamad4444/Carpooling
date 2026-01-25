
import os
import shutil

# Config
OLD_PKG = "de.hskl.swtp.carpooling"
NEW_PKG = "com.rideshare.app"
OLD_PATH = "carpooling-app/src/main/java/de/hskl/swtp/carpooling"
NEW_PATH = "carpooling-app/src/main/java/com/rideshare/app"
OLD_TEST_PATH = "carpooling-app/src/test/java/de/hskl/swtp/carpooling"
NEW_TEST_PATH = "carpooling-app/src/test/java/com/rideshare/app"

def replace_in_file(filepath):
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
        
        new_content = content.replace(OLD_PKG, NEW_PKG)
        
        if content != new_content:
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(new_content)
            print(f"Updated {filepath}")
    except Exception as e:
        print(f"Error processing {filepath}: {e}")

def move_and_update(old_dir, new_dir):
    if not os.path.exists(old_dir):
        print(f"Directory not found: {old_dir}")
        return

    for root, dirs, files in os.walk(old_dir):
        # Determine relative path from old_dir
        rel_path = os.path.relpath(root, old_dir)
        target_root = os.path.join(new_dir, rel_path)
        
        if not os.path.exists(target_root):
            os.makedirs(target_root)
            
        for file in files:
            if file.endswith(".java"):
                src_file = os.path.join(root, file)
                dst_file = os.path.join(target_root, file)
                shutil.move(src_file, dst_file)
                replace_in_file(dst_file)

# Main Execution
print("Moving Main Sources...")
move_and_update(OLD_PATH, NEW_PATH)

print("Moving Test Sources...")
move_and_update(OLD_TEST_PATH, NEW_TEST_PATH)

# Clean up empty directories
# shutil.rmtree("carpooling-app/src/main/java/de") # Be careful not to delete if other stuff exists
# shutil.rmtree("carpooling-app/src/test/java/de")

print("Done.")
